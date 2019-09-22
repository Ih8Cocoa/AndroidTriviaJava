package com.meow.androidtrivia;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
    private List<String> answers = new ArrayList<>();
    private int questionIndex = 0;
    private final int numQuestions = Math.min((questions.size() + 1) / 2, 3);

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
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentGameBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game, container, false
        );
        randomizeQuestions();
        binding.setGame(this);
        binding.submitButton.setOnClickListener(view -> {
            int checkedId = binding.questionRadioGroup.getCheckedRadioButtonId();
            if (checkedId != -1) {
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
                if (Objects.equals(answers.get(answerIndex), currentQuestion.answers.get(0))) {
                    questionIndex++;
                    if (questionIndex < numQuestions) {
                        // to next question
                        currentQuestion = questions.get(questionIndex);
                        setQuestions();
                        // refresh data in binding
                        binding.invalidateAll();
                    } else {
                        // TODO: WON!
                        throw new UnsupportedOperationException("Not implemented");
                    }
                } else {
                    // TODO: LOSE!
                    throw new UnsupportedOperationException("Not implemented");
                }
            }
        });
        return binding.getRoot();
    }

    private void addQuestion(String q, String a1, String a2, String a3, String a4) {
        List<String> answers = Arrays.asList(a1, a2, a3, a4);
        Question qa = new Question(q, answers);
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
        Collections.copy(answers, currentQuestion.answers);
        Collections.shuffle(answers);
        // Set question title
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            ActionBar bar = appCompatActivity.getSupportActionBar();
            if (bar != null) {
                String str = getString(
                        R.string.title_android_trivia_question,
                        questionIndex + 1, numQuestions
                );
                bar.setTitle(str);
            }
        }
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