package vinay.com.observerdesignpatterntesting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import vinay.com.observerdesignpatterntesting.cart.CartItem;
import vinay.com.observerdesignpatterntesting.networking.CartItemSchema;
import vinay.com.observerdesignpatterntesting.networking.GetCartItemsHttpEndpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCaseTest {

    public static final int LIMIT = 10;
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final int PRICE = 10;
    FetchCartItemsUseCase mFetchCartItemsUseCase;

    @Mock
    GetCartItemsHttpEndpoint getCartItemsHttpEndpointMock;

    @Mock
    FetchCartItemsUseCase.Listener mListenerMock1;

    @Mock
    FetchCartItemsUseCase.Listener mListenerMock2;

    @Captor
    ArgumentCaptor<List<CartItem>> listArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        mFetchCartItemsUseCase = new FetchCartItemsUseCase(getCartItemsHttpEndpointMock);
        success();
    }

    //correct limit passed to the endpoint
    @Test
    public void fetchCartItems_correctLimitsPassedToEndPoint() {
        //Arrange
        ArgumentCaptor<Integer> acInt = ArgumentCaptor.forClass(Integer.class);
        //Act
        mFetchCartItemsUseCase.fetchCartItemsAndNotify(LIMIT);
        //Assert
        verify(getCartItemsHttpEndpointMock).getCartItems(acInt.capture(), any(GetCartItemsHttpEndpoint.Callback.class));
        assertThat(acInt.getValue(), is(LIMIT));
    }

    //success - all observers notified with correct data
    @Test
    public void fetchCartItems_success_observersNotifiedwithCorrectData() {
        //Arrange
        //Act
        mFetchCartItemsUseCase.registerListener(mListenerMock1);
        mFetchCartItemsUseCase.registerListener(mListenerMock2);
        mFetchCartItemsUseCase.fetchCartItemsAndNotify(LIMIT);

        //Assert
        verify(mListenerMock1).onCartItemsFetched(listArgumentCaptor.capture());
        verify(mListenerMock2).onCartItemsFetched(listArgumentCaptor.capture());


        List<List<CartItem>> captures = listArgumentCaptor.getAllValues();
        List<CartItem> capture1 = captures.get(0);
        List<CartItem> capture2 = captures.get(1);
        assertThat(capture1, is(getCartItems()));
        assertThat(capture2, is(getCartItems()));

    }



    //success - unsubscribe observers not notified
    @Test
    public void fetchCartItems_success_unsubscribedObserversnotNotified() throws Exception{
        //Arrange
        //Act

        mFetchCartItemsUseCase.registerListener(mListenerMock1);
        mFetchCartItemsUseCase.registerListener(mListenerMock2);
        mFetchCartItemsUseCase.unregisterListener(mListenerMock2);

        mFetchCartItemsUseCase.fetchCartItemsAndNotify(LIMIT);

        //Assert

        verify(mListenerMock1).onCartItemsFetched(any(List.class));
        verifyNoMoreInteractions(mListenerMock2);
    }



    //general error - observers notified of failure

    @Test
    public void fetchCartItems_generalError_observersNotifiedwithFailure() {
        //Arrange
        generalError();
        //Act
        mFetchCartItemsUseCase.registerListener(mListenerMock1);
        mFetchCartItemsUseCase.registerListener(mListenerMock2);
        mFetchCartItemsUseCase.fetchCartItemsAndNotify(LIMIT);

        //Assert
        verify(mListenerMock1).onFetchCartItemsFailed();
        verify(mListenerMock2).onFetchCartItemsFailed();
    }



    //network error - observers notified of failure


    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetCartItemsHttpEndpoint.Callback callback = (GetCartItemsHttpEndpoint.Callback) args[1];
                callback.onGetCartItemsSucceeded(getCartItemSchemes());
                return null;
            }
        }).when(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(GetCartItemsHttpEndpoint.Callback.class));
    }

    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetCartItemsHttpEndpoint.Callback callback = (GetCartItemsHttpEndpoint.Callback) args[1];
                callback.onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR);
                return null;
            }
        }).when(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(GetCartItemsHttpEndpoint.Callback.class));
    }

    private List<CartItemSchema> getCartItemSchemes() {
        List<CartItemSchema> schemas = new ArrayList<>();
        schemas.add(new CartItemSchema(ID, TITLE, DESCRIPTION, PRICE));
        return schemas;
    }

    private List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(ID, TITLE, DESCRIPTION, PRICE));
        return cartItems;
    }

}