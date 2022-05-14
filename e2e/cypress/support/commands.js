Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Login').click();
        cy.get('input[name="username"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.contains('button', 'Login').click();
    })
})
