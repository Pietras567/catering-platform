import './App.scss'
import {BrowserRouter, Route, Routes} from "react-router";
import HomePage from "./components/HomePage.tsx";

function App() {

  return (
    <>
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<HomePage />} />
          </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
