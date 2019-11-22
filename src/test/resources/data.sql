INSERT INTO public.organizations (
    id, avatar, cnpj, codigo_erp, email, external_id, name, type, fk_organizations_id
) VALUES (
        public.organizations_seq.nextval, -- 1
--      avatar         cnpj        codigo_erp   email               external_id                          name          type  fk_organizations_id
        null,   '82910893000177',   NULL,       null,   '8db1bc13-1438-4583-ab32-0f47c5e5df1d', 'Accounting Firm Co',   1,          NULL
    ),
    (
        public.organizations_seq.nextval, -- 2
        null,   '99097492000142',   NULL,       null,   '19d6be0c-6d42-4bdd-a331-ee011d412d5d', 'Customer Company Co',  2,           1
    );
    
INSERT INTO public.users (
    id, activated, avatar, email, first_name, last_name, password, phone, type, username, fk_accounting_id
) VALUES (
        public.users_seq.nextval, -- 1
--     activated   avatar               email            first_name   last_name                     password (ottimizza)                     
        true,       NULL,   'administrator@ottimizza.com.br',  NULL,        NULL,    '$2a$10$zLPbutX166HQbG81aMwHfeZJdrQqD9etQ5VXC48J6YcY7t1THJFsa', 
--     phone   type                username            fk_accounting_id                     
        NULL,      0,      'administrator@ottimizza.com.br',       NULL
    );
