declare namespace Cypress{
    interface Chainable {
        /**
         * Navigate to main page and login as admin
         */
        loginAdmin();

        /**
         * Navigate to main page and register as user
         */
        registerUser();

        /**
         * Navigate to artist page and upload an image
         */
         uploadImage();
    }
}
