package com.jewelry.repair.service.impl;

import com.jewelry.annotation.MenuAuthAnt;
import com.jewelry.file.domain.FileTO;
import com.jewelry.file.mapper.FileMapper;
import com.jewelry.file.service.AmazonS3Service;
import com.jewelry.repair.domain.RepairTO;
import com.jewelry.repair.domain.RepairVO;
import com.jewelry.repair.mapper.RepairMapper;
import com.jewelry.repair.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

	private final RepairMapper repairMapper;
	
	private final AmazonS3Service amazonS3Service;
	
	private final FileMapper fileMapper;
	
	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllRepair(RepairTO to) {
		Map<String, Object> response = new HashMap<>();

		to.setTotalcount(repairMapper.selectRepairListCount(to));
		response.put("list", repairMapper.selectRepairList(to));
		response.put("params", to);
		
		return response;
	}


	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public RepairVO findRepair(RepairTO to) {
		RepairVO vo = repairMapper.selectRepair(to.getRepair_no());
		if(vo != null) vo.setFilelist(fileMapper.selectFileListByRefInfo(new FileTO(to.getRepair_no(), "REPAIR")));
		return vo;
	}


	@Transactional
	@MenuAuthAnt
	@Override
	public String insertRepair(RepairTO to) {
		String result = "success";

		try {
			
			int res = repairMapper.insertRepair(to);
			if(res > 0) {

				FileTO fileto = amazonS3Service.upload(to.getRepairfile(), "repair", "REPAIR");
				
				Long repair_no = to.getRepair_no();
				
				if(repair_no != null && repair_no > 0) {
					if(!ObjectUtils.isEmpty(fileto.getOrigin_nm())) {
						fileto.setInpt_id(to.getInpt_id());
						fileto.setRef_no(repair_no);
						fileMapper.insertFile(fileto);
					}
				}

				result = "success";
			}
			else {
				result = "fail";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}

		return result;
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String updateRepair(RepairTO to) {
		String result = "success";

		try {
			int res = repairMapper.updateRepair(to);
			if(res > 0) {

				FileTO fileto = amazonS3Service.upload(to.getRepairfile(), "repair", "REPAIR");
				
				Long repair_no = to.getRepair_no();
				
				if(repair_no != null && repair_no > 0) {
					if(!ObjectUtils.isEmpty(fileto.getOrigin_nm())) {
						FileTO delfileto = new FileTO();
						delfileto.setRef_no(repair_no);
						delfileto.setRef_info("REPAIR");
						delfileto.setUpdt_id(to.getUpdt_id());
						fileMapper.updateFileToDeleteWithRef(delfileto);
						
						fileto.setInpt_id(to.getInpt_id());
						fileto.setRef_no(repair_no);
						fileMapper.insertFile(fileto);
					}
				}

				result = "success";
			}
			else {
				result = "fail";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}

		return result;
	}

	@Transactional
	@MenuAuthAnt
	@Override
	public String updateRepairToDelete(RepairTO to) {
		String result = "success";

		try {
			int res = repairMapper.updateRepairToDelete(to);
			result = res > 0 ? "success" : "fail";
		}
		catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}

		return result;
	}
	
	@Transactional
	@MenuAuthAnt
	@Override
	public String updateRepairsToDelete(RepairTO to) {
		String result = "fail";
		try {
			Long[] repair_no_arr = to.getRepair_no_arr();
			if(repair_no_arr != null && repair_no_arr.length > 0) {
				int res = repairMapper.updateRepairsToDelete(to);
				result = res > 0 ? "success" : "fail";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}

		return result;
	}
	
	@Transactional
	@MenuAuthAnt
	@Override
	public String updateRepairsToComplete(RepairTO to) {
		String result = "fail";
		try {
			Long[] repair_no_arr = to.getRepair_no_arr();
			if(repair_no_arr != null && repair_no_arr.length > 0) {
				int res = repairMapper.updateRepairsToComplete(to);
				result = res > 0 ? "success" : "fail";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}

		return result;
	}

	@Transactional(readOnly = true)
	@MenuAuthAnt
	@Override
	public Map<String, Object> findAllCustomerRepair(RepairTO to) {
		Map<String, Object> response = new HashMap<>();

		to.setTotalcount(repairMapper.selectCustomerRepairListCount(to));
		response.put("list", repairMapper.selectCustomerRepairList(to));
		response.put("params", to);
		
		return response;
	}
}
