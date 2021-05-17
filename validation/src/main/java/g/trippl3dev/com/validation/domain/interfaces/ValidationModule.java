package g.trippl3dev.com.validation.domain.interfaces;

import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import g.trippl3dev.com.validation.data.ValidationTag;
import g.trippl3dev.com.validation.domain.CommonTypes;
import g.trippl3dev.com.validation.domain.ValidationToast;
import kotlin.Triple;

public class ValidationModule {
    private ValidationToastMethod validationToast;
    private ValidationChecker.OverallValidationChecker overallValidationChecker;
    private SparseArray<ValidationChecker> validationCheckerSparseArray = new SparseArray<>();


    private ValidationModule() {
        registerCommonTypes();
    }

    public static ValidationModule get() {
        return new ValidationModule();
    }

    private void registerCommonTypes() {
        setDefaultToast(ValidationToast.ToastTypes.SNACKBAR);
        addType(CommonTypes.EditTextType.EDITTEXT, new CommonTypes.EditTextType());
    }

    public ValidationModule setToastMethod(ValidationToastMethod toastMethod) {
        this.validationToast = toastMethod;
        return this;
    }

    public ValidationModule setDefaultToast(@ValidationToast.ToastTypes int validationType) {
        ValidationToast validationToast = new ValidationToast();
        validationToast.setType(validationType);
        this.validationToast = validationToast;
        return this;
    }

    public ValidationModule addType(int type, ValidationChecker validationChecker) {
        validationChecker.setValidationModule(this);
        validationCheckerSparseArray.put(type, validationChecker);
        return this;
    }


    private boolean isIgnore(View view) {
        if (view.getTag() instanceof ValidationTag)
            return ((ValidationTag) view.getTag()).isIgnore();
        return false;
    }


    public boolean validate(View view) {
        Triple<Boolean,String, View> validationResult= validateView(view);
        if (validationResult.getFirst()){
            if(overallValidationChecker != null)
            overallValidationChecker.onValid();
        }else{
            if(overallValidationChecker != null)
            overallValidationChecker.onNotValidError(validationResult.getSecond(),validationResult.getThird());
        }
        return validationResult.getFirst();
    }

    private Triple<Boolean,String,View> validateView(View view) {
        if (isIgnore(view)) return new Triple(true,"",view);
        if (view instanceof ViewGroup) {
            if (view.getVisibility() != View.VISIBLE) return new Triple(true,"",view);
            Triple<Boolean, String, View> validateParentView = validateData(view);
            if (!validateParentView.getFirst()) return validateParentView;
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View viewChild = ((ViewGroup) view).getChildAt(i);
                Triple<Boolean, String, View> validateView = validateView(viewChild);
                if (!validateView.getFirst()) return validateView;
            }
        } else {
            if (view.getVisibility() != View.VISIBLE) return new Triple(true,"",view);
            Triple<Boolean, String, View> validateData = validateData(view);
            if (!validateData.getFirst()) return validateData;
        }
        return validateData(view);
    }

    private Triple<Boolean,String,View> validateData(View view) {
    if (!(view.getTag() instanceof ValidationTag)) return new Triple(true,"",view);
        ValidationTag validationTag = (ValidationTag) view.getTag();
        if (validationTag == null) return new Triple(true,"",view);
        ValidationChecker checker = validationCheckerSparseArray.get(validationTag.getViewType());
        if(checker == null){
            return new Triple(true,"",view);
        }
        return checker.checking(validationTag, view);
    }



/***************************************************/
public boolean validateIFValid(View view) {
    Triple<Boolean,String, View> validationResult= validateViewIFValid(view);
    if (validationResult.getFirst()){
        overallValidationChecker.onValid();
    }else{
        overallValidationChecker.onNotValid();
    }
    return validationResult.getFirst();
}

    private Triple<Boolean,String,View> validateViewIFValid(View view) {
        if (isIgnore(view)) return new Triple(true,"",view);
        if (view instanceof ViewGroup) {
            if (view.getVisibility() != View.VISIBLE) return new Triple(true,"",view);
            Triple<Boolean, String, View> validateParentView = validateDataIFValid(view);
            if (!validateParentView.getFirst()) return validateParentView;
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View viewChild = ((ViewGroup) view).getChildAt(i);
                Triple<Boolean, String, View> validateView = validateViewIFValid(viewChild);
                if (!validateView.getFirst()) return validateView;
            }
        } else {
            if (view.getVisibility() != View.VISIBLE) return new Triple(true,"",view);
            Triple<Boolean, String, View> validateData = validateDataIFValid(view);
            if (!validateData.getFirst()) return validateData;
        }
        return validateDataIFValid(view);
    }

    private Triple<Boolean,String,View> validateDataIFValid(View view) {
        if (!(view.getTag() instanceof ValidationTag)) return new Triple(true,"",view);
        ValidationTag validationTag = (ValidationTag) view.getTag();
        if (validationTag == null) return new Triple(true,"",view);
        ValidationChecker checker = validationCheckerSparseArray.get(validationTag.getViewType());
//        if (checker != null)
//        if (!validationTag.isValid())
//            checker.watcher.onNotValid(validationTag.getErrorText(),view);
        return new Triple<>(validationTag.isValid(),validationTag.getErrorText(),view);
    }
/***************************************************/
    public void enableWatching(View view) {
        if (isIgnore(view)) return;
        if (view instanceof ViewGroup) {
            if (view.getVisibility() != View.VISIBLE) return;
            watchView(view);
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View viewChild = ((ViewGroup) view).getChildAt(i);
                enableWatching(viewChild);
            }
        } else {
            if (view.getVisibility() != View.VISIBLE) return;
            watchView(view);
        }
    }

    private void watchView(View view) {
        ValidationTag validationTag = (ValidationTag) view.getTag();
        if (validationTag == null || !validationTag.isWatch()) return;
        ValidationChecker checker = validationCheckerSparseArray.get(validationTag.getViewType());
        if (checker == null) return;
        checker.setWatching(view, validationTag);
        validateView(view);
    }


    /*****************/


    public ValidationToastMethod getValidationToast() {
        return validationToast;
    }

    public ValidationModule setOverallValidationChecker(ValidationChecker.OverallValidationChecker overallValidationChecker) {
        this.overallValidationChecker = overallValidationChecker;
        return this;
    }
    public ValidationChecker.OverallValidationChecker getOverallValidationChecker(){
        return  this.overallValidationChecker ;
    }
}
