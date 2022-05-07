Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('a', 'Login').click();
        cy.get('input[name="username"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.contains('button', 'Login').click();
    })
})

Cypress.Commands.add('createMessage', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.contains('a', 'Message');
        cy.contains('button', 'Add message').click();
        cy.get('input[name="title"]').type('title' +  msg);
        cy.get('textarea[name="summary"]').type('summary' +  msg);
        cy.get('textarea[name="text"]').type('text' +  msg);
        cy.get('button[id="add-msg"]').click();
        cy.get('button[id="close-modal-btn"]').click();

        cy.contains('title' +  msg).should('be.visible');
        cy.contains('summary' +  msg).should('be.visible');
    })
})
