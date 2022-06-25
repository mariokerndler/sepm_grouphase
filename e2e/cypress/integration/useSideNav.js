context('useSideNav', () => {
    it('use', () => {
        cy.loginAdmin();
        cy.useSideNav();
    });
});
