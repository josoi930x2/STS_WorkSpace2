package net.datasa.web3;

import lombok.Data;

/**
 * 회원정보를 저장하여 전달할 클래스
 */
@Data
public class PersonDTO {
	String id;
	String name;
	int age;
}
