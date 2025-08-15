import CircularProgress from "@mui/material/CircularProgress";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";

export default function AchievementCard({ title, value, label }) {
  return (
    <div className="achievement-card">
      <Box position="relative" display="inline-flex">
        <CircularProgress
          variant="determinate"
          value={value}
          size={90}
          thickness={5}
          sx={{ color: "#ff9800", backgroundColor: "#2a2f37" }}
        />
        <Box
          top={0}
          left={0}
          bottom={0}
          right={0}
          position="absolute"
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <Typography variant="h5" component="div" color="#fff">
            {`${value}%`}
          </Typography>
        </Box>
      </Box>
      <div className="achievement-title">{title}</div>
      <div className="achievement-label">{label}</div>
    </div>
  );
}
