package personnel;


public class ExceptionsEmploye {
   
    public static class DatesIncoherentes extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public DatesIncoherentes() {
            super("La date de départ ne peut pas être antérieure à la date d'arrivée");
        }
        
        public DatesIncoherentes(String message) {
            super(message);
        }
    }
   
    public static class DateArriveeNulle extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public DateArriveeNulle() {
            super("La date d'arrivée ne peut pas être nulle");
        }
        
        public DateArriveeNulle(String message) {
            super(message);
        }
    }
}