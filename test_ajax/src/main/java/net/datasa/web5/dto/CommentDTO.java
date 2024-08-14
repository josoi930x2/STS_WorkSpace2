package net.datasa.web5.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	
	private Integer num;
	private String name;
	private String comment;
	
}
