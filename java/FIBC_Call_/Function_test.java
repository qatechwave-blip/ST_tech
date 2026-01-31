package FIBC_Call_;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Function_Adaptix_test.AddArticle_Form;
import Function_Adaptix_test.Login_form;
import Function_STB_Adaptix_test.test_Base;
import Listener_Class.TestListener;
import utilities.JiraUtils;

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
        Function_Adaptix_test.FIBC_Module fibc = new Function_Adaptix_test.FIBC_Module(admindriver);
        fibc.fibcModule();
    }

    @Test(priority = 3)
    public void masterModule() {
        Function_Adaptix_test.Master_Group master = new Function_Adaptix_test.Master_Group(admindriver);
        master.masterBTN();
    }

    @Test(priority = 4)
    public void work_AddArticle() {
        AddArticle_Form add = new AddArticle_Form(admindriver);

        add.workBTN()
           .PlusIcon()
           .selectCustomer(1)
           .selectArticleByIndex(1)
           .selectProductByIndex(1)
           .selectArticleTypeByIndex(1)
           .selectPanelTypeByIndex(1)
           .selectOrderType(1)
           .selectFIBCGradeByIndex(1)
           .selectFIBCTypeByIndex(1)
           .selectSafetyFactorByIndex(1)
           .enterSWL_KG("1.223")
           .enterBagDimension("100x80x80")
           .enterBagWeight("2.5")
           .clickFabricLamination()
           .selectPrintingColorByIndex(1)
           .selectPrintingSideByIndex(1)
           .enterBodyQty("2000")
           .enterColor("Black")
           .enterGSM("2.1");

     // Soft assert trigger with Jira creation on failure
        try {
            add.verifyAll();   // SoftAssert.assertAll()
        } catch (AssertionError e) {

            if (Boolean.parseBoolean(System.getProperty("auto.jira", "false"))) {

                String summary = "SoftAssert Failed: work_AddArticle";
                String description = e.toString();

                String jiraKey = JiraUtils.createBug(
                        summary,
                        description,
                        "Bug"
                );

                if (jiraKey != null) {
                    System.out.println("Jira created for SoftAssert: " + jiraKey);
                }
            }

            throw e; // 🔥 mandatory to fail test
        }

    }
}
