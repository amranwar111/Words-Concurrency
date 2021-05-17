package g.trippl3dev.com.validation.domain.interfaces;

import android.util.Pair;
import android.view.View;

import g.trippl3dev.com.validation.data.ValidationTag;
import g.trippl3dev.com.validation.domain.interfaces.Enums.ValidationState;
import kotlin.Triple;

public abstract class ValidationChecker<T> {
     CheckerWatcher watcher;
    private ValidationModule validationModule;

    public ValidationChecker() {
        watcher = getWatcher();
    }

    public abstract int check(ValidationTag validationTag, T view);

    protected final Triple<Boolean, String, View> checking(ValidationTag validationTag, T view) {
        switch (check(validationTag, view)) {
            case ValidationState.EMPTYERROR:
                validationTag.setValid(false);
                if (validationModule.getOverallValidationChecker() != null)
                    validationModule.getOverallValidationChecker().onNotValid();
                watcher.onNotValid(validationTag.getEmptyError(), view);
                return new Triple(false, validationTag.getEmptyError(), view);
            case ValidationState.VALIDATIONERROR:
                validationTag.setValid(false);
                if (validationModule.getOverallValidationChecker() != null)
                    validationModule.getOverallValidationChecker().onNotValid();
                watcher.onNotValid(validationTag.getErrorText(), view);
                return new Triple(false, validationTag.getErrorText(), view);
            case ValidationState.VALID:
                validationTag.setValid(true);
                if (validationModule.getOverallValidationChecker() != null)
                    validationModule.validateIFValid(((View) view).getRootView());
                watcher.onValid(view);
                return new Triple(true, "valid", view);
        }
        return new Triple(true, validationTag.getErrorText(), view);
    }

    public ValidationToastMethod getToasMethod() {
        return validationModule.getValidationToast();
    }

    public CheckerWatcher<T> getWatcher() {
        return new CheckerWatcher<T>() {

            @Override
            public void onNotValid(String error, T view) {
                getToasMethod().showToast((View) view, error);
            }

            @Override
            public void onValid(T view) {

            }
        };
    }

    protected void setWatching(T v, ValidationTag validationTag) {

    }

    protected final void setValidationModule(ValidationModule validationModule) {
        this.validationModule = validationModule;
    }

    public interface OverallValidationChecker {
        void onNotValidError(String error, View view);

        void onValid();

        void onNotValid();
    }
}
