package ru.inettel.cassa.dao;

import javafx.collections.ObservableList;
import ru.inettel.cassa.MainFormController;
import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.AtirraUser;
import ru.inettel.cassa.user.User;
import ru.inettel.cassa.user.UtmUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ksork on 29.06.17.
 */
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
        if (this.searchData.isInet()){
            UtmAsyncReader utmAsyncReader = new UtmAsyncReader();
            utmTread = new Thread(utmAsyncReader);
            utmTread.start();
        }
        if (this.searchData.isTv()){
            AtirraAsyncReader atirraAsyncReader = new AtirraAsyncReader();
            atirraThread = new Thread(atirraAsyncReader);
            atirraThread.start();
        }
        DisableFindBtn disableFindBtn = new DisableFindBtn();
        Thread delayThread = new Thread(disableFindBtn);
        delayThread.start();
        try {
            delayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void searcUsers(SearchData searchData, ObservableList<User> usersList, MainFormController mainFormController){
        new DAOHelper(searchData, usersList, mainFormController);
    }

    public static Set<String> selectNames(){
        Set<String> names = new HashSet<>();
        BillingDAO utmDao = new UtmDAO();
        BillingDAO atirraDao = new AtirraDAO();
        try {
            utmDao.startConnect();
            atirraDao.startConnect();
            Set<String> utmNames = utmDao.selectNames();
            Set<String> atirraNames = atirraDao.selectNames();
            utmDao.stopConnect();
            atirraDao.stopConnect();
            names.addAll(utmNames);
            names.addAll(atirraNames);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return names;
    }

    private class UtmAsyncReader implements Runnable{
        @Override
        public void run() {
            List<User> selectedUsers = new ArrayList<>();
            BillingDAO dao = new UtmDAO();
            try {
                dao.startConnect();
                selectedUsers = dao.selectUsers(searchData);
                dao.stopConnect();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            usersList.addAll(selectedUsers);
        }
    }

    private class AtirraAsyncReader implements Runnable{
        @Override
        public void run() {
            List<User> selectedUsers = new ArrayList<>();
            BillingDAO dao = new AtirraDAO();
            try {
                dao.startConnect();
                selectedUsers = dao.selectUsers(searchData);
                dao.stopConnect();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            usersList.addAll(selectedUsers);
        }
    }

    private class DisableFindBtn implements Runnable{
        @Override
        public void run() {
            mainFormController.disableFindBtn(true);
            try {
                if(utmTread != null)
                    utmTread.join();
                if (atirraThread != null)
                    atirraThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainFormController.disableFindBtn(false);
        }
    }
}
