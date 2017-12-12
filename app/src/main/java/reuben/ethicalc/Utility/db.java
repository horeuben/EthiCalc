package reuben.ethicalc.Utility;

        import android.view.View;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import reuben.ethicalc.Database.User;

/**
 * Created by linweili on 11/12/17.
 */

public class db {
    private static FirebaseDatabase mFireBaseDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mShopsDatabaseReference = mFireBaseDatabase.getReference().child("shops");
    private static DatabaseReference mCompaniesDatabaseReference = mFireBaseDatabase.getReference().child("companies");
    private static DatabaseReference mUsersDatabaseReference = mFireBaseDatabase.getReference().child("users");
    private static DatabaseReference mProductsDatabaseReference = mFireBaseDatabase.getReference().child("products");
    private static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    //get data from users
    final static String uid = mFirebaseAuth.getUid();
    final static String username = "Cyrus Wang";

//    public static void main(String[] args) {
//        mUsersDatabaseReference.child(uid).setValue(new User(username,"500.32","56.21","53.42","2.79","28012.34","5341.32","48.12","50.23","50.01","52.21"));
//    }
}
