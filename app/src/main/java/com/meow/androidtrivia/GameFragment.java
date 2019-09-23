package com.meow.androidtrivia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meow.androidtrivia.databinding.FragmentGameBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameFragment extends Fragment {
    private List<Question> questions = new ArrayList<>();
    private Question currentQuestion;
    private List<String> answers;
    private int questionIndex = 0;
    private final int numQuestions;

    // just add a bunch of questions and answers
    public GameFragment() {
        addQuestion(
                "What is Android Jetpack?",
                "all of these", "tools", "documentation", "libraries"
        );
        addQuestion(
                "Base class for Layout?",
                "ViewGroup", "ViewSet", "ViewCollection", "ViewRoot"
        );
        addQuestion(
                "Layout for complex Screens?",
                "ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout"
        );
        addQuestion(
                "Pushing structured data into a Layout?",
                "Data Binding", "Data Pushing", "Set Text", "OnClick"
        );
        addQuestion(
                "Inflate layout in fragments?",
                "onCreateView", "onActivityCreated", "onCreateLayout", "onInflateLayout"
        );
        addQuestion(
                "Build system for Android?",
                "Gradle", "Graddle", "Grodle", "Groyle"
        );
        addQuestion(
                "Android vector format?",
                "VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector"
        );
        addQuestion(
                "Android Navigation Component?",
                "NavController", "NavCentral", "NavMaster", "NavSwitcher"
        );
        addQuestion(
                "Registers app with launcher?",
                "intent-filter", "app-registry", "launcher-registry", "app-launcher"
        );
        addQuestion(
                "Mark a layout for Data Binding?",
                "<layout>", "<binding>", "<data-binding>", "<dbinding>"
        );
        // after adding, finalize the number of questions
        numQuestions = Math.min((questions.size() + 1) / 2, 3);
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        final FragmentGameBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game, container, false
        );
        randomizeQuestions();

        // bind the XML variable to this entire fragment
        binding.setGame(this);

        // set on-click listener for the submit button
        binding.submitButton.setOnClickListener(view -> {
            final int checkedId = binding.questionRadioGroup.getCheckedRadioButtonId();
            // if nothing is checked (the result will be -1), exit immediately
            if (checkedId == -1) {
                return;
            }
            int answerIndex = 0;
            switch (checkedId) {
                case R.id.second_answer_radio_button2:
                    answerIndex = 1;
                    break;
                case R.id.third_answer_radio_button3:
                    answerIndex = 2;
                    break;
                case R.id.fourth_answer_radio_button4:
                    answerIndex = 3;
                    break;
            }
            // The first answer in the original list will always be the correct one
            if (Objects.equals(answers.get(answerIndex), currentQuestion.answers.get(0))) {
                questionIndex++;
                if (questionIndex < numQuestions) {
                    // to next question
                    currentQuestion = questions.get(questionIndex);
                    setQuestions();
                    // refresh data in binding, and return early
                    binding.invalidateAll();
                    return;
                }
                // (4) We've won here. Navigate to gameWonFragment
                // Find our current view, then start the navigation
                final NavDirections gameWonTarget =
                        GameFragmentDirections.actionGameFragmentToGameWonFragment(
                                // provide number of questions and correct answers
                                numQuestions, questionIndex
                        );
                Navigation.findNavController(view).navigate(gameWonTarget);
                // (5) also return early to reduce code nesting
                return;
            }
            // (6) You lost. Find our current view, then navigate to the gameOverFragment
            final NavDirections gameOverTarget =
                    GameFragmentDirections.actionGameFragmentToGameOverFragment(
                            // provide number of questions and correct answers
                            numQuestions, questionIndex
                    );
            Navigation.findNavController(view).navigate(gameOverTarget);
        });
        return binding.getRoot();
    }

    private void addQuestion(String q, String a1, String a2, String a3, String a4) {
        final List<String> answers = Arrays.asList(a1, a2, a3, a4);
        final Question qa = new Question(q, answers);
        questions.add(qa);
    }

    private void randomizeQuestions() {
        // shuffle the list
        Collections.shuffle(questions);
        questionIndex = 0;
        setQuestions();
    }

    private void setQuestions() {
        currentQuestion = questions.get(questionIndex);
        // copy out the answers
        answers = new ArrayList<>(currentQuestion.answers);
        // and shuffle the answers
        Collections.shuffle(answers);
        // Set question title
        final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        // if appCompatActivity is null, exit early
        if (appCompatActivity == null) {
            return;
        }
        final ActionBar bar = appCompatActivity.getSupportActionBar();
        // same with support for ActionBar
        if (bar == null) {
            return;
        }
        final String str = getString(
                R.string.title_android_trivia_question,
                questionIndex + 1, numQuestions
        );
        bar.setTitle(str);
    }

    public class Question {
        private String text;
        private List<String> answers;

        private Question(String text, List<String> answers) {
            if (answers.size() != 4) {
                throw new IllegalArgumentException("Number of answers must be 4");
            }
            this.text = text;
            this.answers = answers;
        }

        public String getText() {
            return text;
        }
    }
}