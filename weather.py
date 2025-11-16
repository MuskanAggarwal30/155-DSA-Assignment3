from typing import List, Dict, Optional

# Weather Record ADT
class WeatherRecord:
    def __init__(self, date: str, city: str, temperature: float):
        self.date = date  # Format: "DD/MM/YYYY"
        self.city = city
        self.temperature = temperature

    def __repr__(self):
        return f"WeatherRecord(date={self.date}, city={self.city}, temperature={self.temperature})"

# Data Storage Class
class WeatherDataStorage:
    def __init__(self, years: List[str], cities: List[str]):
        self.years = years
        self.cities = cities
        # 2D array, rows: years, columns: cities. None as sentinel for missing/sparse data
        self.data = [[None for _ in cities] for _ in years]

    # Insert temperature for a year/city
    def insert(self, year: str, city: str, temperature: float) -> None:
        try:
            i = self.years.index(year)
            j = self.cities.index(city)
            self.data[i][j] = temperature
        except ValueError:
            pass

    # Delete temperature for a year/city
    def delete(self, year: str, city: str) -> None:
        try:
            i = self.years.index(year)
            j = self.cities.index(city)
            self.data[i][j] = None
        except ValueError:
            pass

    # Retrieve temperature for a year/city
    def retrieve(self, year: str, city: str) -> Optional[float]:
        try:
            i = self.years.index(year)
            j = self.cities.index(city)
            return self.data[i][j]
        except ValueError:
            return None

    # Populate array with WeatherRecord instances
    def populateArray(self, records: List[WeatherRecord]) -> None:
        for record in records:
            year = record.date.split('/')[-1]
            self.insert(year, record.city, record.temperature)

    # Row-major access (by year, then city)
    def rowMajorAccess(self) -> None:
        print("Row-major Order:")
        for i, year in enumerate(self.years):
            for j, city in enumerate(self.cities):
                print(f"Year: {year}, City: {city}, Temp: {self.data[i][j]}")

    # Column-major access (by city, then year)
    def columnMajorAccess(self) -> None:
        print("Column-major Order:")
        for j, city in enumerate(self.cities):
            for i, year in enumerate(self.years):
                print(f"Year: {year}, City: {city}, Temp: {self.data[i][j]}")

    # This returns a sparse representation (dict of (year-city) -> temp)
    def handleSparseData(self) -> Dict[str, float]:
        sparse = {}
        for i, year in enumerate(self.years):
            for j, city in enumerate(self.cities):
                if self.data[i][j] is not None:
                    sparse[f"{year}-{city}"] = self.data[i][j]
        return sparse

    # Analyze complexity for operations
    def analyzeComplexity(self, op: str) -> str:
        if op in ("insert", "delete", "retrieve"):
            return "Time: O(1), Space: O(n*m)"
        elif op in ("rowMajorAccess", "columnMajorAccess"):
            return "Time: O(n*m), Space: O(n*m)"
        elif op == "handleSparseData":
            return "Time: O(n*m), Space: O(k) where k = number of non-sparse records"
        else:
            return "Operation not defined"

# Demo/test section
if __name__ == "__main__":
    years = ["2024", "2025"]
    cities = ["Chandigarh", "Amritsar", "Ludhiana"]

    # Sample records
    records = [
        WeatherRecord("01/09/2024", "Chandigarh", 34.2),
        WeatherRecord("01/09/2024", "Amritsar", 33.1),
        WeatherRecord("01/09/2024", "Ludhiana", 32.5),
        WeatherRecord("01/09/2025", "Amritsar", 31.6),
    ]

    db = WeatherDataStorage(years, cities)
    db.populateArray(records)

    # Demonstrate required operations
    db.rowMajorAccess()
    db.columnMajorAccess()

    print("Retrieve Chandigarh 2024:", db.retrieve("2024", "Chandigarh"))
    db.delete("2024", "Chandigarh")
    print("After delete Chandigarh 2024:", db.retrieve("2024", "Chandigarh"))

    print("Sparse Records:", db.handleSparseData())

    print("Complexity insert:", db.analyzeComplexity("insert"))
    print("Complexity rowMajorAccess:", db.analyzeComplexity("rowMajorAccess"))
    print("Complexity handleSparseData:", db.analyzeComplexity("handleSparseData"))