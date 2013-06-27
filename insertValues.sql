insert into AUTOS ("state_number", "brand", "driver_id", "under_repair") values 
('A111AA177RUS', 'Aston Martin', 1, 0);
insert into AUTOS ("state_number", "brand", "driver_id", "under_repair") values 
('B222BB177RUS', 'Bentley', 2, 0);
insert into AUTOS ("state_number", "brand", "driver_id", "under_repair") values 
('C333CC199RUS', 'Chevrolet Cruze', 3, 0);
insert into DRIVERS ("name", "phone", "auto_id", "on_vacation") values
('Alferov', '+7-915-5633427', 1, 0);
insert into DRIVERS ("name", "phone", "auto_id", "on_vacation") values
('Borisov', '+7-916-9342745', 2, 1);
insert into DRIVERS ("name", "phone", "auto_id", "on_vacation") values
('Carev', '+7-965-0483672', 3, 0);
insert into CLIENTS ("name", "address", "person", "phone", "saldo") values
('Автофрамос','109316, Москва, Волгоградский просп., 42, корп.36','Silantiev Oleg', '+7-495-3672347', '1938000');
insert into CLIENTS ("name", "address", "person", "phone", "saldo") values
('Шатура мебель','Москва, ул. Академика Скрябина, 1','Максимов Петр', '+7-495-3785690', '349000');
insert into EXECUTERS ("name", "address", "phone") values
('Logistic ADR Group','Moscow, Petrovka, 24','+7-499-3496784');
insert into EXECUTERS ("name", "address", "phone") values
('TransLog','Moscow, Kuusinena, 2','+7-499-7845678');
insert into OBJECTS ("name", "person", "address", "phone", "client_id") values
('Renault', 'Michele Lemuer', 'Moscow, Nagatinskaya, 1', '+7-495-6784573', 1);
insert into ORDERS ("start_date", "end_date", "executer_id", "driver_id", "client_id", "object_id", "load", 
"bill_passed", "waybill_passed") values 
('2013-06-25 19:45:00', '2013-06-26 22:45:00', 1, 1, 2, 2, 'Снег', 1, 1);
insert into ORDERS ("start_date", "end_date", "executer_id", "driver_id", "client_id", "object_id", "load", 
"bill_passed", "waybill_passed") values 
('2013-06-25 19:45:00', '2013-06-26 11:15:00', 1, 3, 2, 2, 'Песок', 1, 0);
insert into ORDERS ("start_date", "end_date", "executer_id", "driver_id", "client_id", "object_id", "load", 
"bill_passed", "waybill_passed") values 
('2013-06-26 11:25:00', '2013-06-26 11:45:00', 1, 2, 1, 1, 'Щебень', 0, 1);