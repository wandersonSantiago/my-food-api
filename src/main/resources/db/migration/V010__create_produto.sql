create table produto (

		id int8 generated by default as identity, 
		ativo boolean not null, 
		descricao varchar(200) not null, 
		nome varchar(60) not null, 
		preco numeric(19, 2) not null, 
		restaurante_id int8 not null, 
		primary key (id)
		
		)
		
		

