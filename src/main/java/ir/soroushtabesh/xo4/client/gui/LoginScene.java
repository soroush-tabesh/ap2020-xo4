package ir.soroushtabesh.xo4.client.gui;


import ir.soroushtabesh.xo4.client.utils.FXUtil;
import javafx.scene.control.Alert;

public class LoginScene extends AbstractScene {
    public void showError() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , ""
                , Messages.LOGIN_PAGE_DIALOG_ERROR);
    }

    public void showExists() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_SIGN_UP
                , Messages.LOGIN_PAGE_DIALOG_EXISTS);
    }

    public void showWrong() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_LOGIN
                , Messages.LOGIN_PAGE_DIALOG_WRONG);
    }

    public void showCheckInput() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , ""
                , Messages.LOGIN_PAGE_DIALOG_CHECK_INPUT);
    }

    public void showSignUpSuccess() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_SIGN_UP
                , Messages.LOGIN_PAGE_DIALOG_SUCCESS);
    }

    public void showCantConnect() {
        FXUtil.showAlert(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_SIGN_UP
                , Messages.LOGIN_PAGE_DIALOG_CONNECTION, Alert.AlertType.ERROR);
    }
}
