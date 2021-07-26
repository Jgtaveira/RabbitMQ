package com.microservico.estoquepreco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservico.estoquepreco.constantes.RabbitMQConstantes;
import com.microservico.estoquepreco.dto.EstoqueDTO;
import com.microservico.estoquepreco.service.RabbitmqService;

@RestController
@RequestMapping(value = "estoque")
public class EstoqueController {

	@Autowired
	private RabbitmqService rabbitmqService;

	@PutMapping
	private ResponseEntity alteraEstoque(@RequestBody EstoqueDTO estoqueDto) {
		System.out.println(estoqueDto.codigoProduto);
		System.out.println(estoqueDto.quantidade);

		this.rabbitmqService.enviaMensagemCriptografada(RabbitMQConstantes.FILA_ESTOQUE, estoqueDto);
		return new ResponseEntity(HttpStatus.OK);
	}
}
