package FIBC_Call_;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Function_Adaptix_test.AddArticle_Form;
import Function_Adaptix_test.Login_form;
import Function_STB_Adaptix_test.test_Base;
import Listener_Class.TestListener;

@Listeners(TestListener.class)
public class Function_test extends test_Base {

    @Test(priority = 1)
    public void loginPage() {
        Login_form login = new Login_form(admindriver);
        login.setUserName("techwave");
        login.setUserPass("concordstb@12345");
        login.signINBTN();
    }

    @Test(priority = 2)
    public void FIBC() {
        Function_Adaptix_test.FIBC_Module fibc =
                new Function_Adaptix_test.FIBC_Module(admindriver);
        fibc.fibcModule();
    }

    @Test(priority = 3)
    public void masterModule() {
        Function_Adaptix_test.Master_Group master =
                new Function_Adaptix_test.Master_Group(admindriver);
        master.masterBTN();
    }

    @Test(priority = 4)
    public void work_AddArticle() {

        AddArticle_Form add = new AddArticle_Form(admindriver);

        add.workBTN()
           .PlusIcon()

           // Dropdown selections
           .selectCustomer("Techwave Customer")   // use actual visible text
           .selectArticleByIndex(1)
           .selectProductByIndex(1)
           .selectArticleTypeByIndex(1)
           .selectPanelTypeByIndex(1)
           .selectOrderType("Regular")
           .selectFIBCGradeByIndex(1)
           .selectFIBCTypeByIndex(1)
           .selectSafetyFactorByIndex(1)

           // Text inputs
           .enterSWL_KG("1.223")
           .enterBagDimension("100x80x80")
           .enterBagWeight("2.5")

           // Toggle + dropdowns
           .clickFabricLamination()
           .selectPrintingColorByIndex(1)
           .selectPrintingSideByIndex(1)

           // Components (ag-grid)
           .enterBodyQty("2000")
           .enterColor("Black")
           .enterGSM("2.1");

        // Final soft assert trigger
        add.verifyAll();
    }

}
