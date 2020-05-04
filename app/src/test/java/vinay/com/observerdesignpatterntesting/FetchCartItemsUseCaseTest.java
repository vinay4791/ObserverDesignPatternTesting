package vinay.com.observerdesignpatterntesting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCaseTest {

    FetchCartItemsUseCase mFetchCartItemsUseCase;

    @Before
    public void setUp() throws Exception {
        mFetchCartItemsUseCase = new FetchCartItemsUseCase();
    }

    //correct limit passed to the endpoint
    @Test
    public void fetchCartItems_correctLimitsPassedToEndPoint(){

    }

    //success - all observers notified with correct data

    //success - unsubscribe observers not notified

    //general error - observers notified of failure

    //network error - observers notified of failure



}