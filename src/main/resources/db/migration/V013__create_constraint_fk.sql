alter table cidade add constraint Fk_cidade_estado foreign key (estado_id) references estado;
		
alter table grupo_permissao add constraint Fk_grupopermissao_permissao foreign key (permissao_id) references permissao;

alter table grupo_permissao add constraint Fk_grupopermissoa_grupo foreign key (grupo_id) references grupo;

alter table restaurante add constraint Fk_restaurante_cozinha foreign key (cozinha_id) references cozinha;

alter table restaurante add constraint Fk_restaurante_cidade foreign key (endereco_cidade_id) references cidade;

alter table produto add constraint Fk_produto_restaurante foreign key (restaurante_id) references restaurante;

alter table restaurante_forma_pagamento add constraint Fk_restauranteformapagamento_forma_pagamento foreign key (forma_pagamento_id) references forma_pagamento;

alter table restaurante_forma_pagamento add constraint Fk_restauranteformapagamento_restaurante foreign key (restaurante_id) references restaurante;
