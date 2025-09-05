package in.eightfolds.winga.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ShowCaseUtils {

    private static boolean isPresentingShowcase = false; //Restrict to show 2 showcaseViews at once


    private static String HOME_SHOWCASE_ID = "Home sequence";
    private static String HOME_SHOWCASE_PLAY_ID = "Home play sequence";
    private static String PROFILE_SHOWCASE_ID = "Profile sequence";
    private static String VIDEO_SHOWCASE_ID = "Video sequence";
    private static String QUESTIONS_SHOWCASE_ID = "Questions sequence";
    private static String RESULTS_SHOWCASE_ID = "Results sequence";


    private static boolean conditionsMetForShow(Context context) {
        if (Constants.showQuickTour && !isPresentingShowcase) {
            boolean isShowCaseSkipped = EightfoldsUtils.getInstance().getBooleanFromSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED);
            if (!isShowCaseSkipped) {
                return true;
            }
        }
        return false;
    }

    public static void presentHomeShowcaseSequence(final Context context, View playIv) {

//        if (!conditionsMetForShow(context)) {
//            return;
//        }
//
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(1000); // half second between each showcase view
//
//        final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, HOME_SHOWCASE_PLAY_ID);
//
//
//        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//            @Override
//            public void onShow(MaterialShowcaseView itemView, int position) {
//                EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_HOME_TUTORIAL_SHOW_IN_NO_SESSION, false);
//                isPresentingShowcase = true;
//
//            }
//        });
//
//        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
//            @Override
//            public void onDismiss(MaterialShowcaseView itemView, int position) {
//
//                    isPresentingShowcase = false;
//
//            }
//        });
//
//        sequence.setOnShowSkippedListener(new MaterialShowcaseSequence.OnSequenceSkipListener() {
//
//            @Override
//            public void onSkip() {
//                SkipShowCase(context);
//            }
//        });
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(playIv)
//                        .setDismissText(context.getString(R.string.got_it))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setContentText(context.getString(R.string.info_play_button))
//                        .setDismissOnTargetTouch(true)
//                        .setDismissOnTouch(true)
//                        .withRectangleShape()
//                        .setMaskColour(R.color.tutorial_color)
//                        .build()
//        );
//        //
//
//
//        sequence.start();

    }

    public static void presentHomeShowcaseSequence(final Context context, final View playIv, View profileRL, View pointsRL, View yesterdayWinnersBtn) {

//        if (!conditionsMetForShow(context)) {
//            return;
//        }
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//
//        final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, HOME_SHOWCASE_ID);
//
//
//        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//            @Override
//            public void onShow(MaterialShowcaseView itemView, int position) {
//                isPresentingShowcase = true;
//                if (playIv == null && position >= 2) {
//
//                    EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_HOME_TUTORIAL_SHOW_IN_NO_SESSION, true);
//                }
//            }
//        });
//
//        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
//            @Override
//            public void onDismiss(MaterialShowcaseView itemView, int position) {
//                if (playIv == null) {
//                    if (position == 2) {
//                        isPresentingShowcase = false;
//                    }
//                } else {
//                    if (position == 3) {
//                        isPresentingShowcase = false;
//                    }
//                }
//
//            }
//        });
//
//        sequence.setOnShowSkippedListener(new MaterialShowcaseSequence.OnSequenceSkipListener() {
//
//            @Override
//            public void onSkip() {
//                SkipShowCase(context);
//            }
//        });
//
//        sequence.setConfig(config);
//        if (playIv != null) {
//            sequence.addSequenceItem(
//                    new MaterialShowcaseView.Builder((Activity) context)
//                            .setTarget(playIv)
//                            .setDismissText(context.getString(R.string.next))
//                            .setSkipText(context.getString(R.string.skip_underline))
//                            .setContentText(context.getString(R.string.info_play_button))
//                            .setDismissOnTargetTouch(true)
//                            .setDismissOnTouch(true)
//                            .withRectangleShape()
//                            .build()
//            );
//        }
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(profileRL)
//                        .setDismissText(context.getString(R.string.next))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setContentText(context.getString(R.string.tutorial_profile))
//                        .setDismissOnTargetTouch(true)
//                        .setDismissOnTouch(true)
//                        .withCircleShape()
//                        .build()
//        );
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(pointsRL)
//                        .setDismissText(context.getString(R.string.next))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setContentText(context.getString(R.string.tutorial_history))
//                        .setDismissOnTargetTouch(true)
//                        .setDismissOnTouch(true)
//                        .withCircleShape()
//                        .build()
//        );
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(yesterdayWinnersBtn)
//                        .setDismissText(context.getString(R.string.done))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setContentText(context.getString(R.string.tutorial_recent_winners))
//                        .setDismissOnTargetTouch(true)
//                        .setDismissOnTouch(true)
//                        .withCircleShape()
//                        .build()
//        );
//
//        sequence.start();

    }


    public static void presentResultsShowcaseSequence(final Context context, View scratchLL) {

//        try {
//
//            if (!conditionsMetForShow(context)) {
//                return;
//            }
//            ShowcaseConfig config = new ShowcaseConfig();
//            config.setDelay(500); // half second between each showcase view
//
//            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, ShowCaseUtils.RESULTS_SHOWCASE_ID);
//
//            sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//                @Override
//                public void onShow(MaterialShowcaseView itemView, int position) {
//                    //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
//                    isPresentingShowcase = true;
//                }
//            });
//
//            sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
//                @Override
//                public void onDismiss(MaterialShowcaseView itemView, int position) {
//
//                    isPresentingShowcase = false;
//
//                }
//            });
//
//            sequence.setOnShowSkippedListener(new MaterialShowcaseSequence.OnSequenceSkipListener() {
//
//                @Override
//                public void onSkip() {
//                    EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED, true);
//                }
//            });
//
//            sequence.setConfig(config);
//
//            sequence.addSequenceItem(
//                    new MaterialShowcaseView.Builder((Activity) context)
//                            .setTarget(scratchLL)
//                            .setDismissText(context.getString(R.string.got_it))
//                            .setSkipText(context.getString(R.string.skip_underline))
//                            .setContentText(context.getString(R.string.tutorial_scratch))
//                            .setDismissOnTouch(true)
//                            .withCircleShape()
//                            .build()
//            );
//
//
//            sequence.start();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }


    public static void presentQuestionsAnswersShowcaseSequence(final Context context, View watchVideoIv, View nextBtn) {
//
//        if (!conditionsMetForShow(context)) {
//            return;
//        }
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, ShowCaseUtils.QUESTIONS_SHOWCASE_ID);
//
//        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//            @Override
//            public void onShow(MaterialShowcaseView itemView, int position) {
//                //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
//                isPresentingShowcase = true;
//            }
//        });
//
//        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
//            @Override
//            public void onDismiss(MaterialShowcaseView itemView, int position) {
//
//                if(position == 1) {
//                    isPresentingShowcase = false;
//                }
//
//            }
//        });
//
//        sequence.setOnShowSkippedListener(new MaterialShowcaseSequence.OnSequenceSkipListener() {
//
//            @Override
//            public void onSkip() {
//                EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED, true);
//            }
//        });
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(watchVideoIv)
//                        .setDismissText(context.getString(R.string.next))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setContentText(context.getString(R.string.tutorial_watch_video_again))
//                        .setDismissOnTouch(true)
//                        .withCircleShape()
//                        .build()
//        );
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(nextBtn)
//                        .setDismissText(context.getString(R.string.got_it))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setContentText(context.getString(R.string.tutorial_submit_answers))
//                        .setDismissOnTouch(true)
//                        .withRectangleShape()
//                        .build()
//        );
//
//
//        sequence.start();

    }


    public static void presentVideoShowcaseSequence(final Context context, View askQstnBtn,String title) {

//        if (!conditionsMetForShow(context)) {
//            return;
//        }
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, ShowCaseUtils.VIDEO_SHOWCASE_ID);
//
//        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//            @Override
//            public void onShow(MaterialShowcaseView itemView, int position) {
//                //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
//                isPresentingShowcase = true;
//            }
//        });
//
//        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
//            @Override
//            public void onDismiss(MaterialShowcaseView itemView, int position) {
//
//                    isPresentingShowcase = false;
//
//            }
//        });
//
//        sequence.setOnShowSkippedListener(new MaterialShowcaseSequence.OnSequenceSkipListener() {
//
//            @Override
//            public void onSkip() {
//                EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED, true);
//            }
//        });
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(askQstnBtn)
//                        .setDismissText(context.getString(R.string.got_it))
//                        .setContentText(title)
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setDismissOnTouch(true)
//                        .withCircleShape()
//                        .build()
//        );
//
//
//        sequence.start();

    }

    public static void presentProfileShowcaseSequence(final Context context, View editIv) {

//        if (!conditionsMetForShow(context)) {
//            return;
//        }
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, ShowCaseUtils.PROFILE_SHOWCASE_ID);
//
//        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//            @Override
//            public void onShow(MaterialShowcaseView itemView, int position) {
//                //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
//                isPresentingShowcase = true;
//            }
//        });
//
//        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
//            @Override
//            public void onDismiss(MaterialShowcaseView itemView, int position) {
//
//                isPresentingShowcase = false;
//
//            }
//        });
//
//        sequence.setOnShowSkippedListener(new MaterialShowcaseSequence.OnSequenceSkipListener() {
//
//            @Override
//            public void onSkip() {
//                EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED, true);
//            }
//        });
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder((Activity) context)
//                        .setTarget(editIv)
//                        .setDismissText(context.getString(R.string.got_it))
//                        .setSkipText(context.getString(R.string.skip_underline))
//                        .setSkipToLeft()
//                        .setContentText(context.getString(R.string.tutorial_edit_profile))
//                        .setDismissOnTouch(true)
//                        .withCircleShape()
//                        .build()
//        );
//
//
//        sequence.start();

    }

    private static void SkipShowCase(Context context) {
        EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED, true);
        isPresentingShowcase = false;
    }

    public static void RestartShowCaseView(Context context) {
        EightfoldsUtils.getInstance().saveBooleanToSharedPreference(context, Constants.IS_TUTORIAL_SKIPPED, false);
        ResetShowCaseIDs();
    }

    public static void ResetShowCaseIDs() {
        long time = System.currentTimeMillis();

        HOME_SHOWCASE_ID = "Home sequence"  + time;
        HOME_SHOWCASE_PLAY_ID = "Home play sequence" + time;
        PROFILE_SHOWCASE_ID = "Profile sequence"  + time;
        VIDEO_SHOWCASE_ID = "Video sequence"  + time;
        QUESTIONS_SHOWCASE_ID = "Questions sequence"  + time;
        RESULTS_SHOWCASE_ID = "Results sequence"  + time;
    }
}
