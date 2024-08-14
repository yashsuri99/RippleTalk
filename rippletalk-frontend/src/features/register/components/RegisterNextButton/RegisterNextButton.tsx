import styled from 'styled-components';
import { StyledNextButtonProps } from '../../../../utils/GlobalInterfaces';

export const StyledNextButton = styled.button<StyledNextButtonProps>`
	width: 100%;
	height: 52px;
	color: white;
	font-size: 17px;
	background-color: ${(props) =>
		props.color === 'blue'
			? props.theme.colors.blue
			: props.theme.colors.black};
	opacity: ${(props) => (props.active ? 1.0 : 0.5)};
	border-radius: 50px;
	cursor: ${(props) => (props.active ? 'pointer' : 'auto')};
`;
