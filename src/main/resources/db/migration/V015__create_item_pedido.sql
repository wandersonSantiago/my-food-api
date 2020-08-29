create table item_pedido (

		id int8 generated by default as identity, 
		observacao varchar(255),
		preco_total numeric(19, 2), 
		preco_unitario numeric(19, 2), 
		quantidade int4, 
		pedido_id int8 not null, 
		produto_id int8 not null, 
		
		primary key (id),
		
		
		constraint Fk_itempedido_pedido foreign key (pedido_id) references pedido,
		constraint Fk_itempedido_produto foreign key (produto_id) references produto

		
		)