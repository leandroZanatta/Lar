package br.com.lar.server.dto;

import java.util.List;

import lombok.Data;

@Data
public class PesquisaPaginadaDTO<T> {

	private Long numeroRegistros;

	private List<T> rows;
}
