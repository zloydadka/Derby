alter table orders add constraint start_and_end_dates check ("start_date" <= "end_date");

alter table autos add CONSTRAINT driver_id_unq UNIQUE ("driver_id");
alter table drivers add CONSTRAINT auto_id_unq UNIQUE ("auto_id");

alter table orders add CONSTRAINT order_driver_fk FOREIGN KEY ("driver_id")
REFERENCES drivers ("id");
alter table orders add CONSTRAINT order_client_fk FOREIGN KEY ("client_id")
REFERENCES clients ("id");
alter table orders add CONSTRAINT order_object_fk FOREIGN KEY ("object_id")
REFERENCES drivers ("id");
