package com.Loser.dao.domain;

import java.util.Date;

public class AccountFundTargetDetails {
        private int addCash;
        private int addInterest;
        private int currentCash;
        private int currentInterest;
        private int currentAmount;
        private Date targetDate;

        public int getCurrentAmount() {
            return currentAmount;
        }

        public void setCurrentAmount(int currentAmount) {
            this.currentAmount = currentAmount;
        }

        public Date getTargetDate() {
            return targetDate;
        }

        public void setTargetDate(Date targetDate) {
            this.targetDate = targetDate;
        }

        public int getAddCash() {
            return addCash;
        }

        public void setAddCash(int addCash) {
            this.addCash = addCash;
        }

        public int getAddInterest() {
            return addInterest;
        }

        public void setAddInterest(int addInterest) {
            this.addInterest = addInterest;
        }

        public int getCurrentCash() {
            return currentCash;
        }

        public void setCurrentCash(int currentCash) {
            this.currentCash = currentCash;
        }

        public int getCurrentInterest() {
            return currentInterest;
        }

        public void setCurrentInterest(int currentInterest) {
            this.currentInterest = currentInterest;
    }
}
