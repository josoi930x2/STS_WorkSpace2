package net.datasa.web5;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
	
	private String name;
	private Integer age;
	private String phone;
	
}
