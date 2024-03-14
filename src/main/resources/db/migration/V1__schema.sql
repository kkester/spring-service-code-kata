create table catalogs (
    id uuid not null,
    code varchar(255) not null,
    display_name varchar(255),
    primary key (id)
);
alter table catalogs add constraint code_unique unique(code);

create table products (
    id uuid not null,
    sku varchar(255) not null,
    description varchar(255),
    name varchar(255) not null,
    price varchar(255) not null,
    catalog_id varchar(255) not null,
    quantity integer not null,
    primary key (id)
);
alter table products add constraint FKr9g72vsfwc9lupwutnut4w7kf foreign key (catalog_id) references catalogs;
alter table products add constraint catalog_sku_unique unique(catalog_id,sku);