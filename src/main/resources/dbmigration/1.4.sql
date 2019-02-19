-- apply changes
create table disease (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  constraint uq_disease_name unique (name),
  constraint pk_disease primary key (id)
);

create table disease_status (
  id                            integer auto_increment not null,
  status                        boolean default false not null,
  disease_id                    integer,
  consultation_id               integer,
  constraint pk_disease_status primary key (id)
);

create index ix_disease_status_disease_id on disease_status (disease_id);
alter table disease_status add constraint fk_disease_status_disease_id foreign key (disease_id) references disease (id) on delete restrict on update restrict;

create index ix_disease_status_consultation_id on disease_status (consultation_id);
alter table disease_status add constraint fk_disease_status_consultation_id foreign key (consultation_id) references consultation (id) on delete restrict on update restrict;

