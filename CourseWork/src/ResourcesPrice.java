public class ResourcesPrice{
    //змінні класу ResourcesPrice
    private double lfWaterPrice;
    private double lfLightPrice;

    // конструктор за замовчуванням класу ResourcesPrice
    public ResourcesPrice() {
        this.lfLightPrice = 0;
        this.lfWaterPrice = 0;
    }
    // конструктор з параметрами класу ResourcesPrice
    public ResourcesPrice(double lfWaterPrice, double lfLightPrice) {
        this.lfWaterPrice = lfWaterPrice;
        this.lfLightPrice = lfLightPrice;
    }
    // конструктор копіювання класу ResourcesPrice
    public ResourcesPrice(ResourcesPrice resourcesPriceToCopy) {
        this.lfWaterPrice = resourcesPriceToCopy.lfWaterPrice;
        this.lfLightPrice = resourcesPriceToCopy.lfLightPrice;
    }

    //геттери класу ResourcesPrice
    public double getLightPrice() {
        return lfLightPrice;
    }
    public double getWaterPrice() {
        return lfWaterPrice;
    }
    public ResourcesPrice getYearPrices(){
        return this;
    }


    //сеттери класу ResourcesPrice
    public void setLightPrice(double lfLightPrice) {
        this.lfLightPrice = lfLightPrice;
    }
    public void setWaterPrice(double lfWaterPrice) {
        this.lfWaterPrice = lfWaterPrice;
    }
    public void setYearPrices(double lfWaterPrice, double lfLightPrice){
        this.lfWaterPrice = lfWaterPrice;
        this.lfLightPrice = lfLightPrice;}
}