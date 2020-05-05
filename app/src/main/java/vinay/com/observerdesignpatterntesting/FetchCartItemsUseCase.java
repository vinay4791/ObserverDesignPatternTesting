package vinay.com.observerdesignpatterntesting;

import java.util.List;

import vinay.com.observerdesignpatterntesting.cart.CartItem;
import vinay.com.observerdesignpatterntesting.networking.CartItemSchema;
import vinay.com.observerdesignpatterntesting.networking.GetCartItemsHttpEndpoint;

public class FetchCartItemsUseCase {

    private final GetCartItemsHttpEndpoint getCartItemsHttpEndpoint;

    public FetchCartItemsUseCase(GetCartItemsHttpEndpoint getCartItemsHttpEndpoint) {
        this.getCartItemsHttpEndpoint = getCartItemsHttpEndpoint;
    }


    public void fetchCartItemsAndNotify(int limit) {
        getCartItemsHttpEndpoint.getCartItems(limit, new GetCartItemsHttpEndpoint.Callback() {
            @Override
            public void onGetCartItemsSucceeded(List<CartItemSchema> cartItems) {

            }

            @Override
            public void onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason failReason) {

            }
        });
    }


    public void registerListener(Listener mListenerMock1) {

    }

    public interface Listener {
        void onCartItemsFetched(List<CartItem> capture);
    }
}
