package com.jewelry.common.domain;

import com.jewelry.cms.menu.domain.MenuAuthVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonVO {
	
	private String userid;
	private String inptid;
	private String inptnm;
	private String inptdt;
	private String updtid;
	private String updtdt;

	private MenuAuthVO menuAuthVO;

}
