public class ResourcesUse {
    //змінні класу ResourcesUse6
    private double iWaterUse;
    private double iLightUse;


    //коструктор за замовчуванням класу ResourcesUse
    public ResourcesUse(){
        this.iWaterUse = -1;
        this.iLightUse = -1;
    }

    //коструктор з параметрами класу ResourcesUse
    public ResourcesUse(double WaterUse, double LightUse){
        this.iWaterUse = WaterUse;
        this.iLightUse = LightUse;
    }

    //коструктор копій класу ResourcesUse
    public ResourcesUse(ResourcesUse ruFlatExpen) {
        this.iWaterUse = ruFlatExpen.getWaterUse();
        this.iLightUse = ruFlatExpen.getLightUse();
    }

    //геттери класу ResourcesUse
    public double getWaterUse() {
        return iWaterUse;
    }
    public double getLightUse() {
        return iLightUse;
    }

    //сеттери класу ResourcesUse
    public void setWaterUse(double iWaterUse) {
        this.iWaterUse = iWaterUse;
    }
    public void setLightUse(double iLightUse) {
        this.iLightUse = iLightUse;
    }
}