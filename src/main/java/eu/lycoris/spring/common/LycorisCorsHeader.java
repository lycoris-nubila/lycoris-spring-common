package eu.lycoris.spring.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LycorisCorsHeader{
	
	private @NotNull String name;
	private boolean exposed;
	private boolean allowed;
	
}
