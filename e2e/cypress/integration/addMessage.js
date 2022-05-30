context('add message', () => {
    let msgText = 'msg' + new Date().getTime();

    it('login', () => {
        cy.loginAdmin();
    });
    it('create message', () => {
        cy.createMessage(msgText);
    })

});
