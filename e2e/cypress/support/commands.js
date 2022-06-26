import 'cypress-file-upload';

Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Login').click();
        cy.get('input[name="email"]').click();
        cy.get('input[name="email"]').click();
        cy.get('input[name="email"]').type(settings.adminUser);
        cy.get('input[name="password"]').click();
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
        cy.get('input[name="email"]').click();
        cy.get('input[name="email"]').type(settings.email);
        cy.get('input[name="address"]').type(settings.address);
        cy.get('input[name="address"]').type(settings.address);
        cy.get('input[name="username"]').type(settings.username);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.get('input[name="confirm"]').type(settings.adminPw);
        cy.get('button[name="submit-button"]').click();
        cy.visit(settings.baseUrl);
        cy.contains('button', 'Login').click();
        cy.get('input[name="email"]').click();
        cy.get('input[name="email"]').type(settings.email);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.get('button[name="submit-button"]').click();
    })
});

Cypress.Commands.add('useSideNav', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.get('label[for="menu_checkbox"]').click();
        cy.contains('span', 'Artists').click();
        cy.get('label[for="menu_checkbox"]').click();
        cy.contains('span', 'Explore').click();
        cy.get('label[for="menu_checkbox"]').click();
        cy.contains('span', 'Commissions').click();
        cy.get('label[for="menu_checkbox"]').click();
        cy.contains('span', 'Contact').click();
    })
})

Cypress.Commands.add('upgradeToArtist', () => {
    cy.fixture('settings').then(settings => {
        cy.contains('button', 'Profile').click();
        cy.contains('button', 'Edit your page').click();
        cy.get('#mat-tab-label-1-2 > .mat-tab-label-content').click();
        cy.contains('button', 'Upgrade').click();
        cy.contains('button', 'Yes').click();

    });
})


Cypress.Commands.add('uploadImage', () => {
    cy.fixture('settings').then(settings => {
            const fixtureFile = 'image4.png';
            cy.contains('button', 'Profile').click()
            cy.get('#mat-tab-label-2-1').click();
            cy.contains('button', 'Upload new picture').click();
            cy.get('input[name="artworkName"]').type(settings.lastName);
            cy.get('textarea[name="description"]').type(settings.lastName);
            cy.get('input[type="file"]').attachFile(fixtureFile); 
            cy.get('button[name="upload-button"]').click();
    }); 
});
