import 'cypress-file-upload';

Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Login').click();
        cy.get('input[name="email"]').type(settings.adminUser);
        cy.get('input[name="email"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.get('button[name="submit-button"]').click();
        
    })
});

Cypress.Commands.add('registerUser', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Register').click();
        cy.get('input[name="firstname"]').type(settings.firstName);
        cy.get('input[name="firstname"]').type(settings.firstName);
        cy.get('input[name="lastname"]').type(settings.lastName);
        cy.get('input[name="email"]').type(settings.email);
        cy.get('input[name="address"]').type(settings.address);
        cy.get('input[name="username"]').type(settings.username);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.get('input[name="confirm"]').type(settings.adminPw);
        cy.get('button[name="submit-button"]').click();
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Login').click();
        cy.get('input[name="email"]').type(settings.email);
        cy.get('input[name="email"]').type(settings.email);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.get('button[name="submit-button"]').click();
    })
});


Cypress.Commands.add('uploadImage', () => {
    cy.fixture('settings').then(settings => {
            cy.visit(settings.baseUrl);
            cy.contains('button', 'Login').click();
            cy.get('input[name="email"]').click();
            cy.get('input[name="email"]').type('testartist@test.com');
            cy.get('input[name="password"]').type('12345678');
            cy.get('button[name="submit-button"]').click();

            const fixtureFile = 'image4.png';
            cy.contains('button', 'Profile').click()
            cy.contains('button', 'See More').click();
            cy.contains('button', 'Upload new picture').click();
            cy.get('input[name="artworkName"]').type(settings.lastName);
            cy.get('textarea[name="description"]').type(settings.lastName);
            cy.get('input[type="file"]').attachFile(fixtureFile); 
            cy.get('button[name="upload-button"]').click();
      
    }); 
});
