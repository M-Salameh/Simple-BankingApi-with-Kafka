package org.example;


public class WritingHelper
{
    String name;
    String location;
    Double amount;

    public WritingHelper(TransactionInfo transactionInfo)
    {
        name = transactionInfo.getName();
        location = transactionInfo.getLocation();
        amount = transactionInfo.getAmount();
    }
    @Override
    public String toString()
    {
        String ans = "";
        Double x = amount;
        ans += "Name : " + name + " --- Location of Transaction : " + location
                + " --- amount of transaction : " + x.toString();
        return ans;
    }

    public void writeToLog()
    {
        String ans = toString();
        AccountManagerKafkaConsumer.log.info(ans);
    }
}
