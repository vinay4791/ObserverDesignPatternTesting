package vinay.com.observerdesignpatterntesting;

import java.util.ArrayList;
import java.util.List;

import vinay.com.observerdesignpatterntesting.cart.CartItem;
import vinay.com.observerdesignpatterntesting.networking.CartItemSchema;
import vinay.com.observerdesignpatterntesting.networking.GetCartItemsHttpEndpoint;

public class FetchCartItemsUseCase {

    private List<Listener> listeners = new ArrayList<>();
    private final GetCartItemsHttpEndpoint getCartItemsHttpEndpoint;

    public FetchCartItemsUseCase(GetCartItemsHttpEndpoint getCartItemsHttpEndpoint) {
        this.getCartItemsHttpEndpoint = getCartItemsHttpEndpoint;
    }


    public void fetchCartItemsAndNotify(int limit) {
        getCartItemsHttpEndpoint.getCartItems(limit, new GetCartItemsHttpEndpoint.Callback() {
            @Override
            public void onGetCartItemsSucceeded(List<CartItemSchema> cartItems) {
                for (Listener listener : listeners) {
                    listener.onCartItemsFetched(cartItemFromSchemas(cartItems));
                }
            }

            @Override
            public void onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason failReason) {

            }
        });
    }

    private List<CartItem> cartItemFromSchemas(List<CartItemSchema> cartItems) {
        List<CartItem> cartItemList = new ArrayList<>();
        for (CartItemSchema cartItemSchema : cartItems) {
            cartItemList.add(new CartItem(cartItemSchema.getId(), cartItemSchema.getTitle(), cartItemSchema.getDescription(), cartItemSchema.getPrice()));

        }
        return cartItemList;
    }


    public void registerListener(Listener mListener) {
        listeners.add(mListener);
    }

    public void unregisterListener(Listener mListener) {
        listeners.remove(mListener);
    }


    public interface Listener {
        void onCartItemsFetched(List<CartItem> capture);
    }
}
