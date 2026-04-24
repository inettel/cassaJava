package ru.inettel.cassa.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import ru.inettel.cassa.MainFormController;
import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.User;

public class DAOHelper {
    private ObservableList<User> usersList;
    private SearchData searchData;
    private MainFormController mainFormController;
    Thread utmTread;
    Thread atirraThread;

    private DAOHelper(SearchData searchData, ObservableList<User> usersList, MainFormController mainFormController) {
        this.searchData = searchData;
        this.usersList = usersList;
        this.mainFormController = mainFormController;
        if (this.searchData.isInet()) {
            UtmAsyncReader utmAsyncReader = new UtmAsyncReader();
            this.utmTread = new Thread(utmAsyncReader);
            this.utmTread.start();
        }

        if (this.searchData.isTv()) {
            AtirraAsyncReader atirraAsyncReader = new AtirraAsyncReader();
            this.atirraThread = new Thread(atirraAsyncReader);
            this.atirraThread.start();
        }

        DisableFindBtn disableFindBtn = new DisableFindBtn();
        Thread delayThread = new Thread(disableFindBtn);
        delayThread.start();

        try {
            delayThread.join();
        } catch (InterruptedException var7) {
            InterruptedException e = var7;
            e.printStackTrace();
        }

    }

    public static void searcUsers(SearchData searchData, ObservableList<User> usersList, MainFormController mainFormController) {
        new DAOHelper(searchData, usersList, mainFormController);
    }

    public static Set<String> selectNames() {
        Set<String> names = new HashSet();
        BillingDAO utmDao = new UtmDAO();
        BillingDAO atirraDao = new AtirraDAO();

        try {
            ((BillingDAO)utmDao).startConnect();
            ((BillingDAO)atirraDao).startConnect();
            Set<String> utmNames = ((BillingDAO)utmDao).selectNames();
            Set<String> atirraNames = ((BillingDAO)atirraDao).selectNames();
            ((BillingDAO)utmDao).stopConnect();
            ((BillingDAO)atirraDao).stopConnect();
            names.addAll(utmNames);
            names.addAll(atirraNames);
        } catch (SQLException var5) {
            SQLException e = var5;
            e.printStackTrace();
        } catch (ClassNotFoundException var6) {
            ClassNotFoundException e = var6;
            e.printStackTrace();
        } catch (IllegalAccessException var7) {
            IllegalAccessException e = var7;
            e.printStackTrace();
        } catch (InstantiationException var8) {
            InstantiationException e = var8;
            e.printStackTrace();
        }

        return names;
    }

    private class DisableFindBtn implements Runnable {
        private DisableFindBtn() {
        }

        public void run() {
            DAOHelper.this.mainFormController.disableFindBtn(true);

            try {
                if (DAOHelper.this.utmTread != null) {
                    DAOHelper.this.utmTread.join();
                }

                if (DAOHelper.this.atirraThread != null) {
                    DAOHelper.this.atirraThread.join();
                }
            } catch (InterruptedException var2) {
                InterruptedException e = var2;
                e.printStackTrace();
            }

            DAOHelper.this.mainFormController.disableFindBtn(false);
        }
    }

    private class AtirraAsyncReader implements Runnable {
        private AtirraAsyncReader() {
        }

        public void run() {
            List<User> selectedUsers = new ArrayList();
            BillingDAO dao = new AtirraDAO();

            try {
                ((BillingDAO)dao).startConnect();
                selectedUsers = ((BillingDAO)dao).selectUsers(DAOHelper.this.searchData);
                ((BillingDAO)dao).stopConnect();
            } catch (SQLException var4) {
                SQLException exxx = var4;
                exxx.printStackTrace();
            } catch (ClassNotFoundException var5) {
                ClassNotFoundException exx = var5;
                exx.printStackTrace();
            } catch (IllegalAccessException var6) {
                IllegalAccessException e = var6;
                e.printStackTrace();
            } catch (InstantiationException var7) {
                InstantiationException ex = var7;
                ex.printStackTrace();
            }

            DAOHelper.this.usersList.addAll((Collection)selectedUsers);
        }
    }

    private class UtmAsyncReader implements Runnable {
        private UtmAsyncReader() {
        }

        public void run() {
            List<User> selectedUsers = new ArrayList();
            BillingDAO dao = new UtmDAO();

            try {
                ((BillingDAO)dao).startConnect();
                selectedUsers = ((BillingDAO)dao).selectUsers(DAOHelper.this.searchData);
                ((BillingDAO)dao).stopConnect();
            } catch (SQLException var4) {
                SQLException exxx = var4;
                exxx.printStackTrace();
            } catch (IllegalAccessException var5) {
                IllegalAccessException exx = var5;
                exx.printStackTrace();
            } catch (InstantiationException var6) {
                InstantiationException e = var6;
                e.printStackTrace();
            } catch (ClassNotFoundException var7) {
                ClassNotFoundException ex = var7;
                ex.printStackTrace();
            }

            DAOHelper.this.usersList.addAll((Collection)selectedUsers);
        }
    }
}
