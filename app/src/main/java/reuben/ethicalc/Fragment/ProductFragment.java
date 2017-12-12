package reuben.ethicalc.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.PriorityQueue;

import reuben.ethicalc.Activity.MainActivity;
import reuben.ethicalc.Database.Product;
import reuben.ethicalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextView pdtName;
    private TextView pdtPrice;
    private Product product;
    private OnFragmentInteractionListener mListener;
    private Button addToCart;

    public ProductFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(Product product) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putParcelable("product",product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_product, container, false);

        pdtName = rootview.findViewById(R.id.product_textview_name);
        pdtPrice = rootview.findViewById(R.id.product_textview_msrp);
        addToCart = rootview.findViewById(R.id.addPdtToCart);
        product = getArguments().getParcelable("product");
        pdtName.setText(product.getProductName());
        pdtPrice.setText("$" + product.getMSRP());


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.pdtInCart.add(product); //add prodcut to the cart list
                Toast.makeText(getActivity(), "Added to Shopping Cart", Toast.LENGTH_SHORT).show();
                for (Product p : MainActivity.pdtInCart){
                    Log.i("testing",p.getProductName());
                }
            }
        });


        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
