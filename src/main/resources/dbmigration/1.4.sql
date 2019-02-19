-- apply changes
create table disease (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  constraint uq_disease_name unique (name),
  constraint pk_disease primary key (id)
);

create table disease_controlled (
  id                            integer auto_increment not null,
  controlled                    boolean default false not null,
  disease_id                    integer,
  consultation_id               integer,
  constraint pk_disease_controlled primary key (id)
);

create index ix_disease_controlled_disease_id on disease_controlled (disease_id);
alter table disease_controlled add constraint fk_disease_controlled_disease_id foreign key (disease_id) references disease (id) on delete restrict on update restrict;

create index ix_disease_controlled_consultation_id on disease_controlled (consultation_id);
alter table disease_controlled add constraint fk_disease_controlled_consultation_id foreign key (consultation_id) references consultation (id) on delete restrict on update restrict;

