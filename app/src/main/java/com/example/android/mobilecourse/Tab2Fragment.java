package com.example.android.mobilecourse;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

    public class Tab2Fragment extends Fragment {

        private FirebaseAuth mAuth;
        DatabaseReference databaseFilms;
        private EditText inputImage;
        private EditText titleText;
        private TextInputLayout inputTitleContainer;
        private TextInputLayout inputDescriptionContainer;
        private TextInputLayout inputYearContainer;
        private TextInputLayout inputRatingContainer;
        private TextInputLayout inputImageContainer;
        private EditText descriptionText;
        private EditText yearText;
        private EditText ratingNumber;
        private Button submitButton;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_add_film, container, false);

            inputTitleContainer = rootView.findViewById(R.id.input_title_container);
            titleText = rootView.findViewById(R.id.input_title);
            inputYearContainer = rootView.findViewById(R.id.input_year_container);
            inputDescriptionContainer = rootView.findViewById(R.id.input_description_container);
            inputRatingContainer = rootView.findViewById(R.id.input_rating_container);
            descriptionText = rootView.findViewById(R.id.input_description);
            yearText = rootView.findViewById(R.id.input_year);
            ratingNumber = rootView.findViewById(R.id.input_rating);
            submitButton = rootView.findViewById(R.id.add_film_button);
            inputImage = rootView.findViewById(R.id.input_image_url);
            inputImageContainer = rootView.findViewById(R.id.input_image_url_container);

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            databaseFilms = FirebaseDatabase.getInstance().getReference("Films");

            submitButton.setOnClickListener(view -> addFilm());

            return rootView;
        }

        public void addFilm() {
        if (!validate()) {
            submitButton.setEnabled(true);
            return;
        }
        submitButton.setEnabled(false);
            Movie movie = new Movie(
                    titleText.getText().toString(), Long.valueOf(yearText.getText().toString()), Long.valueOf(ratingNumber.getText().toString()),
                    descriptionText.getText().toString(), inputImage.getText().toString()
                );
            databaseFilms.push().setValue(movie);
            ((MainActivity) Objects.requireNonNull(getActivity())).getViewPager().setCurrentItem(0);
        }



    private boolean validate() {
        boolean valid = true;

        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String year = yearText.getText().toString();
        String rating = ratingNumber.getText().toString();

        if (title.isEmpty()) {
            inputTitleContainer.setError(getText(R.string.invalid_title));
            valid = false;
        } else {
            inputTitleContainer.setError(null);
        }
        if (description.isEmpty()) {
            inputDescriptionContainer.setError(getText(R.string.invalid_description));
            valid = false;
        } else {
            descriptionText.setError(null);
        }
        if (rating.isEmpty()) {
            inputRatingContainer.setError(getText(R.string.invalid_rating));
            valid = false;
        } else {
            inputRatingContainer.setError(null);
        }
        if (year.isEmpty() || year.length() < 4) {
            inputYearContainer.setError(getText(R.string.invalid_year));
            valid = false;
        } else {
            yearText.setError(null);
        }
        return valid;
    }
    }


