insert into `user` (`id`, `name`, `email_address`, `password`, `admin`, `status`) values
    ('USR-001', 'Bradley', 'bradbuur@hotmail.com', 'd9b5f58f0b38198293971865a14074f59eba3e82595becbe86ae51f1d9f1f65e', 1, 'OFFLINE');

update `_seq_user` set `next_val` = 2;

insert into `event` (`id`, `name`, `active`, `removable`) values
    ('EVT-0001', 'Aangerand', 1, 1),
    ('EVT-0002', 'Racisme', 1, 1),
    ('EVT-0003', 'Agressie', 1, 1),
    ('EVT-0004', 'Uitgescholden', 1, 1),
    ('EVT-0005', 'Gediscrimineerd', 1, 1),
    ('EVT-0006', 'Anders', 1, 0);

update `_seq_event` set `next_val` = 7;

insert into `question` (`id`, `question`, `answer`) values
    ('FAQ-001', 'Wat doen wij met de meldingen?', 'Alle meldingen worden door FemAlert verzameld. Dit wordt bij ons opgeslagen om omgezet in anonieme informatie. Deze anonieme informatie willen wij  presenteren aan de uitgaansgelegenheden en de overheid. Zo kunnen we er met elkaar voor zorgen dat uitgaansgelegenheden, feest-organisatoren en ordehandhavers weten op welke momenten en locaties incidenten plaatsvinden. Met deze informatie kunnen wij met elkaar op zoek naar oplossingen.'),
    ('FAQ-002', 'Wat gebeurt er precies na het ontvangen van een melding?', 'Na het ontvangen van een melding wordt er aan de hand van de melding gekeken wat de mogelijke vervolgstappen zijn voor de persoon die de melding heeft gedaan (indien er behoefte naar is). Vervolgens wordt de melding geregistreerd en gecategoriseerd in onze databank. Nadat de melding een plek heeft gekregen in onze databank, wordt er gekeken bij voor welke van onze partners/stakeholders deze plaatsing relevant is en worden de vervolgstappen intern of met deze partner/stakeholder besproken.'),
    ('FAQ-003', 'Met wie werkt Femalert?', 'Femalert werkt met verschillende partners of stakeholders die onze visie en missie geloven en ook mee willen werken aan het stap voor stap bouwen aan een toekomst waar iedereen in vrijheid en veiligheid zichzelf kan zijn. Momenteel werkt Femalert met verschillende organisaties die in het uitgaansleven opereren en met plaatselijke gemeenten. Bent u geïnteresseerd in een samenwerking met FemAlert? Klik dan op het kopje hieronder.'),
    ('FAQ-004', 'Hoe kan ik partner worden?', 'Femalert heeft een brede netwerk van gedreven partners die geloven in onze visie en missie. Sta je achter onze visie en missie en heb je interesse in een samenwerking met FemAlert? Klik hier.'),
    ('FAQ-005', 'Hoe zit het met privacy/met wie wordt de informatie gedeeld?', 'Veiligheid en betrouwbaarheid is van cruciaal belang, daarom wordt de verzamelde meldingen van de slachtoffers goed beschermd. Informatie die binnenkomt via de meldingen wordt op anonieme basis gebruikt voor ons onderzoek of samenwerking met onze partners. Ook kunnen de organisaties die met ons samenwerken er op vertrouwen dat de informatie die wordt doorgespeeld betrouwbaar is.');

update `_seq_question` set `next_val` = 6;
