INSERT INTO
    public.organizations (
        id,
        avatar,
        cnpj,
        codigo_erp,
        email,
        external_id,
        name,
        type,
        fk_organizations_id
    )
VALUES
    (
        public.organizations_seq.nextval, -- 1
        null,
        '82910893000177',
        NULL,
        null,
        '8db1bc13-1438-4583-ab32-0f47c5e5df1d',
        'Accounting Firm Co',
        1,
        NULL
    ),
    (
        public.organizations_seq.nextval, -- 2
        null,
        '99097492000142',
        NULL,
        null,
        '19d6be0c-6d42-4bdd-a331-ee011d412d5d',
        'Customer Company Co',
        2,
        1
    );
    