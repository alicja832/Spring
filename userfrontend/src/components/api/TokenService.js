//do napisania XXDDD
export default function TokenService() {
    const paperStyle = { padding: '50px 20px', width: 600, margin: "20px auto" }
    const classes = useStyles();
    const [exercises, setExercises] = useState([]);
    const navigate = useNavigate();
  
    const openSolution = (e) => {
      navigate('/solution/:' + e.target.value);
    }
  
  
    useEffect(() => {
      fetch("http://localhost:8080/exercise/")
        .then(res => res.json())
        .then((result) => {
          console.log('Fetched students:', result); // Dodaj t
          setExercises(result);
        }
        ).catch(error => console.error('Error fetching students:', error));
    }, [])
  
    return (
      <div className={classes.container}>
        <h1>Zadania</h1>
        {
          <Paper elevation={3} style={paperStyle}>
  
            {exercises.map(exercise => (
              <Paper elevation={6} style={{ margin: "10px", padding: "15px", textAlign: "left" }} key={exercise.id}>
                <div className={classes.headerContainer}> <h3> {exercise.name}</h3><Box className={classes.points}>{exercise.maxPoints}</Box></div>
  
                <div>
                  <Box display="flex" flexDirection="column" gap={2}>
                    <Button variant="contained" value={exercise.id} color="secondary" onClick={openSolution}>
                      Wykonaj
                    </Button>
                  </Box>
                </div>
              </Paper>
            ))
            }
          </Paper>
        }
      </div>
  
  
    );
  }
  