context('upload image', () => {
    it('upload', () => {
        cy.registerUser();
        cy.upgradeToArtist();
        cy.uploadImage();
    });
});
