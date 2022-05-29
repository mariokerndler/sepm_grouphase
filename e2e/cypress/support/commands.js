Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Login').click();
        cy.get('input[name="email"]').type(settings.adminUser);
        cy.get('input[name="email"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.get('button[name="submit-button"]').click();
        
    })
})
