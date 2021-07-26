package com.microservico.estoquepreco.connections;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import com.microservico.estoquepreco.constantes.RabbitMQConstantes;

@Component
public class RabbibtMQConnnection {

	private static final String NOME_EXCHANGE = "amqp.direct";
	private AmqpAdmin amqpAdmin;

	// CRIANDO A CONEXAO DENTRO DO CONSTRUTOR, POIS É AUTO CONFIGURÁVEL PELA
	// ANOTAÇÃO @COMPONENT E SERÁ UTILIZADO A INJEÇÃO DE DEPENDÊNCIA.
	// COM ISSO OS RECURSOS CONFIGURADOS DENTRO DO APPLICATION.PROPERTIES SERÃO
	// UTILIZADOS AUTOMATICAMENTE
	public RabbibtMQConnnection(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}

	//DEFINIÇÃO DE FILA
	private Queue fila(String nomeFila) {
		return new Queue(nomeFila, true, false, false);
	}

	// DEFINIÇÃO DA EXCHANGE DIRECT
	private DirectExchange trocaDireta() {
		return new DirectExchange(NOME_EXCHANGE);
	}

	// DEFINIÇÃO DO BINDING
	private Binding relacionamentoDirect(Queue fila, DirectExchange troca) {
		return new Binding(fila.getName(), Binding.DestinationType.QUEUE, troca.getName(), fila.getName(), null);
	}

	// A ANOTAÇÃO POSTCONSTRUCTOR, ASSIM QUE A NOSSA CLASSE FOR CONSTRUÍDA O MÉTODO SERÁ EXECUTADO
	@PostConstruct
	private void adiciona() {
		Queue filaEstoque = this.fila(RabbitMQConstantes.FILA_ESTOQUE);
		Queue filaPreco = this.fila(RabbitMQConstantes.FILA_PRECO);

		DirectExchange troca = this.trocaDireta();

		Binding ligacaoEstoque = this.relacionamentoDirect(filaEstoque, troca);
		Binding ligacaoPreco = this.relacionamentoDirect(filaPreco, troca);

		
		// CRIANDO AS FILAS NO RABBITMQ
		this.amqpAdmin.declareQueue(filaEstoque);
		this.amqpAdmin.declareQueue(filaPreco);

		// CRIANDO AS EXCHANGES NO RABBITMQ
		// COMO É UMA EXCHANGE DEFAULT, OU SEJA, JÁ EXISTENTE.
		// NÃO SERÁ CRIADA E SIM UTILIZADA.
		this.amqpAdmin.declareExchange(troca);
		
		// CRIANDO O BINDING  
		this.amqpAdmin.declareBinding(ligacaoEstoque);
		this.amqpAdmin.declareBinding(ligacaoPreco);
	}
}
