/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

/**
 *
 * @author raver
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Main m = new Main();
        SearchEstate searchEstate = m.getCalculatorPort();
        searchEstate.hello("from tester");
    }
    
    /** Get service port stub for Calculator web service. */
    SearchEstate getCalculatorPort() {
        SearchEstate_Service service = new SearchEstate_Service();
        return service.getSearchEstatePort();
    }
}
