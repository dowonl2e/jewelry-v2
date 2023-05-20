package com.jewelry.customer.domain;

import com.jewelry.common.domain.CommonTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerTO extends CommonTO {
	
	private Long customer_no;
	private String store_cd;
	private String contract_cd;
	private String zipcode;
	private String address1;
	private String address2;
	private String etc;
	private String contractor_nm;
	private String contractor_gen;
	private String contractor_cel;
	private String contractor_birth;
	private String contractor_lunar;
	private String contractor_email;
	private String del_yn;
	private String reg_dt;
	private Long[] customer_no_arr;

}
